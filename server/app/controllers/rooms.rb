# -*- coding: utf-8 -*-
Server::App.controllers :rooms do

  before :create, :enter, :start, :hit, :result do
    login(params)
  end

  before :enter, :show, :users, :timelimit do
    find_room(params)
  end

  post :create, :provides => :json do
    invalid_param_error unless params.key? "title"
    @room = Room.new(:title => params[:title]){|r|
      r.time_limit = params[:time_limit] if params[:time_limit]
      r.owner = @user
    }.save
    @room.to_json
  end

  post :enter, :provides => :json do
    @user.enter_room(@room)
    @room.to_json
  end

  post :start, :provides => :json do
    @user.start_room
    @user.room.to_json
  end

  post :hit, :provides => :json do
    unless params.key? "target_user_id"
      invalid_param_error
    else
      target_id = target_id = params[:target_user_id].to_i
    end
    user_ids = @user.room.member_hash
    error_message(201, "Target user(#{target_user_id}) is unknow user.") unless user_ids.key? target_id

    HitLocation.new(:latitude => params[:latitude], :longitude => params[:longitude], :radius => params[:radius]){|hit|
      hit.user = @user
      hit.target = user_ids[target_id]
      hit.room = @user.room
    }.save.to_json
  end

  get :show, :provides => :json do
    @room.to_json
  end

  get :list, :provides => :json do
    Room.order('created_at desc').limit(20).to_json
  end

  get :users, :provides => :json do
    @room.users.to_json
  end

  get :timelimit, :provides => :json do
    (@room.termination_time.to_time - Time.now).to_i
  end
end
