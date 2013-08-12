# -*- coding: utf-8 -*-
Server::App.controllers :rooms do

  post :create, :provides => :json do
    @room = Room.create(:title => params[:title])
    @room.to_json
  end

  post :enter, :provides => :json do
    room = Room.find_by_id(params[:room_id])
    return unless room
    user = User.find_by_id_and_token(params[:user_id], params[:token])
    return unless user
    user.enter_room(room)
    room.to_json
  end

  post :start, :provides => :json do

  end

  get :show, :provides => :json do
    room = Room.find_by_id(params[:room_id])
    return unless room
    room.to_json
  end

  get :list, :provides => :json do
    Room.order('created_at desc').limit(20).to_json
  end

  get :users, :provides => :json do
    room = Room.find_by_id(params[:room_id])
    return unless room
    room.users.to_json
  end
end
