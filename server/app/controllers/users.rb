Server::App.controllers :users do
  helpers do
    def find_user(params)
      invalid_param_error unless params.key? "user_id"
      user = User.find_by_id(params[:user_id])
      error_message(201, "USER NOT FOUND") unless user
      return user
    end
  end

  before :enter, :exit, :start, :hit do
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
    find_room(params)
    @user.enter_room(@room)
    expire @user.cache_key
    @room.to_json
  end

  post :exit, :provides => :json do
    @user.exit_room
    expire @user.cache_key
    @user.to_json
  end

  post :start, :provides => :json do
    @user.start_room
    expire @user.cache_key
    @user.room.to_json
  end

  post :hit, :provides => :json do
    hit = Hit.new(:latitude => params[:latitude], :longitude => params[:longitude]){|hit|
      hit.user = @user
      hit.room = @user.room
    }.save
    hit.to_json
  end
end
