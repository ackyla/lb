# -*- coding: utf-8 -*-
Server::App.controllers :rooms do

  # get :index, :map => '/foo/bar' do
  #   session[:foo] = 'bar'
  #   render 'index'
  # end

  # get :sample, :map => '/sample/url', :provides => [:any, :js] do
  #   case content_type
  #     when :js then ...
  #     else ...
  # end

  # get :foo, :with => :id do
  #   'Maps to url '/foo/#{params[:id]}''
  # end

  # get '/example' do
  #   'Hello world!'
  # end

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
