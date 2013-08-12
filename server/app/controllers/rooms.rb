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

  get :list, :provides => :json do
    Room.all.order('created_at desc'.limit(20).to_json
  end
end
