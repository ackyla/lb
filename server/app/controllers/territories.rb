# -*- coding: utf-8 -*-
Server::App.controllers :territories do
  before :create do
    login(params)
  end

  before :destroy, :locations, :supply, :move, :detections do
    login(params)
    @territory = @user.my_territories.find(params[:id])
  end

  post :create, :provides => :json do
    ter = @user.add_territory(params[:latitude], params[:longitude], params[:character_id])
    res = {territory: ter.to_hash, user: ter.owner.to_hash}
    JSON.unparse(res)
  end

  post :destroy, :provides => :json do
    @territory.expire
    @territory.save
    @territory.to_json
  end

  get :locations, :provides => :json do
    @territory.locations.to_json
  end

  get :detections, provides: :json do
    ret = {territory: @territory.to_hash}
    if @territory.detections.size > 0
      ret = ret.merge({locations: @territory.detections.map{|d| d.location.to_hash }})
    end
    JSON.unparse ret
  end

  post :supply, provides: :json do
    return status_failure "GPSポイントが指定されていません." unless params[:gps_point]
    point = params[:gps_point].to_i
    return status_failure unless @territory.supply point
    return JSON.unparse(status_ok({supplied_point: point, terrritory: @territory.to_hash}))
  end

  post :move, provides: :json do
    coord = Coordinate.find_or_create(lat: params[:latitude], long: params[:longitude])
    @territory.coordinate = coord
    @territory.save
    @territory.to_json
  end
end
