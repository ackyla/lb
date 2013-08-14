Server::App.controllers :locations do
  post :create, :provides => :json do
    login(params)
    Location.new(:latitude => params[:latitude], :longitude => params[:longitude]){|l|
      l.user = user
      l.room = user.room
    }.save.to_json
  end
end
