Server::App.controllers :users do
  helpers do
    def find_user(params)
      error_code(1, "INVALID PARAMETER") unless params.key? "user_id"
      user = User.find_by_id(params[:user_id])
      unless user
        error_code(1, "USER NOT FOUND")
      end
      return user
    end
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
    find_user(params).to_json
  end

  get :locations do
    find_user(params).current_locations.to_json
  end
end
