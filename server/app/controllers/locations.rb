Server::App.controllers :locations do
  post :create, :provides => :json do
    login(params)
    loc = @user.add_location(params[:latitude], params[:longitude])
    Territory.all.each{|ter|
      ter.add_location(loc)
    }
    loc.save
    loc.to_json
  end
end
