# -*- coding: utf-8 -*-
Server::App.controllers :rooms do

  before :enter, :show, :users do
    find_room(params)
  end

  post :create, :provides => :json do
    valid_user(params)
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
    valid_user(params)
    @user.start_room
    @user.room.to_json
  end

  post :hit, :provides => :json do
    valid_user(params)
    user_ids = @user.room.users.inject({}){|r, u|
      r[u.id] = u
      r
    }
    if users_ids.key? "target_user_id"
      target_id = params[:target_user_id]
    else
      error_message(1, "USER IS NOT MEMBER")
    end
    hit_loc = HitLocation.new(
      latitude: params[:latitude],
      longitude: params[:longitude],
      radius: params[:radius]){|hl|
      hl.user = user
      hl.target = user_ids[target_id]
      hl.room = room
    }

    hit_loc.to_json
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
