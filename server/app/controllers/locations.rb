Server::App.controllers :locations do
  post :create, :provides => :json do
    user = User.find_by_id_and_token(params[:user_id], params[:token])
    return unless user
    Location.new(:latitude => params[:latitude], :longitude => params[:longitude]){|l|
      l.user = user
      l.room = user.room
    }.save.to_json
  end
end
