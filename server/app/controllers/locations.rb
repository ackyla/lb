Server::App.controllers :locations do
  post :create, :provides => :json do
    login(params)
    error_message(300, "Cannot post location to inactive room.") unless @user.room.active
    loc = Location.new(:latitude => params[:latitude], :longitude => params[:longitude]){|l|
      l.user = @user
      l.room = @user.room
    }
    loc.save
    loc.to_json
  end

  get :list, :provides => :json, :cache => true do
    expires_in 60
    find_room(params)
    @room.locations.joins(:users).to_json
  end
end
