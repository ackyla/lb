Server::App.controllers :territories do
  post :create, :provides => :json do
    territory = Territory.new(
      :latitude => params[:latitude],
      :longitude => params[:longitude],
      :radius => params[:radius]
      ){|territory|
      territory.user = @user
    }
    territory.save
    territory.to_json
  end

  post :destroy, :provides => :json do
    territory = Territory.find_by_id(params[:id])
    territory.valid = false
  end
end
