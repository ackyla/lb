Server::App.controllers :locations do
  post :create, :provides => :json do
    login(params)
    #error_message("300", "Room is not active") unless @user.room.active
    loc = Location.new(:latitude => params[:latitude], :longitude => params[:longitude]){|l|
      l.user = @user
    }
    loc.save
    loc.to_json
  end
end
