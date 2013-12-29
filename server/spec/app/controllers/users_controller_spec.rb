# -*- coding: utf-8 -*-
require 'spec_helper'

describe "UsersController" do
  before do
    @params = {name: "user_1"}
    post "/users/create", @params
  end

  it "status check" do
    expect(last_response).to be_ok
  end

  it "name check" do
    json = JSON.parse last_response.body
    expect(json["name"]).to eq(@params[:name])
  end
end
