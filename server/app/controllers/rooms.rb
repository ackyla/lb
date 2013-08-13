# -*- coding: utf-8 -*-
Server::App.controllers :rooms do

  before :enter, :show, :users do
    find_room(params)
  end

  post :create, :provides => :json do
    valid_user
    @room = Room.new(:title => params[:title]){|r|
      r.owner = @user
    }.save
    @room.to_json
  end

  post :enter, :provides => :json do
    valid_user(params)
    @user.enter_room(@room)
    @room.to_json
  end

  post :start, :provides => :json do
    valid_user
    @user.start_room
    @user.room.to_json
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
end
