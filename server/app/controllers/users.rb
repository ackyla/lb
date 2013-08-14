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

  get :show, :provides => :json, :cache => true do
    expires_in 60
    cache_key request.path_info + "?user_id=#{params[:user_id]}"
    find_user(params).to_json(:methods => [:room], :except => [:token])
  end

  get :locations do
    find_user(params).current_locations.to_json
  end

  post :enter, :provides => :json do
    find_room(params)
    @user.enter_room(@room)
    @room.to_json
  end

  post :exit, :provides => :json do
    @user.exit_room
    @user.to_json
  end

  post :start, :provides => :json do
    @user.start_room
    @user.room.to_json
  end

  post :hit, :provides => :json do
    unless params.key? "target_user_id"
      invalid_param_error
    else
      target_id = target_id = params[:target_user_id].to_i
    end
    user_ids = @user.room.member_hash
    error_message(201, "Target user(#{target_user_id}) is unknow user.") unless user_ids.key? target_id

    HitLocation.new(:latitude => params[:latitude], :longitude => params[:longitude], :radius => params[:radius]){|hit|
      hit.user = @user
      hit.target = user_ids[target_id]
      hit.room = @user.room
    }.save.to_json
  end
end
