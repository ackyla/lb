Server::App.controllers :locations do
  post :create, :provides => :json do
    login(params)
    loc = Location.new(:latitude => params[:latitude], :longitude => params[:longitude]){|l|
      l.user = @user
    }

    Territory.all{|territory|
      territory.locations << loc if territory.include? loc
    }
    loc.save

    loc.to_json
  end
end
