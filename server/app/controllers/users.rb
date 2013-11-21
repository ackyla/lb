Server::App.controllers :users do
  helpers do
    def find_user(params)
      invalid_param_error unless params.key? "user_id"
      user = User.find_by_id(params[:user_id])
      error_message(201, "USER NOT FOUND") unless user
      return user
    end
  end

  before :enter, :exit, :start, :hit, :territory_list, :create_territory do
    login(params)
  end

  get :create do
    @user = User.new
    render 'users/create'
  end

  post :create, :provides => :json do
    @user = User.create(:name => params[:name])
    content_type :json
    @user.to_json
  end

  get :show, :provides => :json do
    user = find_user(params)
    cache(user.cache_key, expires_in: 10){
      user.to_json(:methods => [:room], :except => [:token])
    }
  end

  post :enter, :provides => :json do
    expire @user.cache_key
    find_room(params)
    @user.enter_room(@room)

    @room.to_json
  end

  post :exit, :provides => :json do
    expire @user.cache_key
    @user.exit_room
    @user.to_json
  end

  post :start, :provides => :json do
    expire @user.cache_key
    @user.start_room
    @user.room.to_json
  end

  post :hit, :provides => :json do
    expire @user.cache_key
    hit = Hit.new(:latitude => params[:latitude], :longitude => params[:longitude]){|hit|
      hit.user = @user
      hit.room = @user.room
    }
    hit.save

    result = Result.new{|r|
      r.score = 100
      r.user = @user
      r.room = @user.room
      r.hit = hit
    }
    result.save

    @user.room = nil
    @user.save!

    hit.to_json
  end

  get :territory_list, :map => '/users/territories/list', :provides => :json do
    expire @user.cache_key
    @user.territories.to_json
  end

  post :create_territory, :map => '/users/territories/create', :provides => :json do
    expire @user.cache_key
    territory = Territory.new(:latitude => params[:latitude], :longitude => params[:longitude], :radius => params[:radius]){|territory|
      territory.user = @user
    }
    territory.save

    territory.to_json
  end
end
