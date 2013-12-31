Server::App.controllers :territories do
  before :create, :destroy, :locations do
    login(params)
  end

  before :destroy, :locations, :supply do
    @territory = Territory.find_by_id(params[:id])
  end

  post :create, :provides => :json do
    ter = @user.add_territory(params[:latitude], params[:longitude], params[:character_id])
    ter.to_json
  end

  post :destroy, :provides => :json do
    @territory.expire
    @territory.save
    @territory.to_json
  end

  get :locations, :provides => :json do
    @territory.locations.to_json
  end

  post :supply, :provides => :json do
    point = params[:gps_point]
    error_message 100, "error" unless point
    if @user.supply(@territory, point)
      JSON.unparse({supplied_point: point, terrritory: @territory.to_hash})
    else
      {error: "error"}
    end
  end
end
