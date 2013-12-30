Server::App.controllers :users do
  helpers do
    def find_user(params)
      invalid_param_error unless params.key? "user_id"
      user = User.find_by_id(params[:user_id])
      error_message(201, "USER NOT FOUND") unless user
      return user
    end
  end

  before :territories, :notifications do
    login(params)
  end

  get :create do
    @user = User.new
    render 'users/create'
  end

  post :create, :provides => :json do
    @user = User.create(:name => params[:name])
    @user.to_json
  end

  get :show, :provides => :json do
    user = find_user(params)
    user.to_json
  end

  get :territories, :provides => :json do
    @user.valid_territories.to_json
  end

  get :notifications, :provides => :json do
    @user.notifications.unread.to_json
  end
end
