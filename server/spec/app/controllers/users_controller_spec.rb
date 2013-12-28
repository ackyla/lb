# -*- coding: utf-8 -*-
require 'spec_helper'

describe "UsersController" do
  describe "User生成" do
    before do
      @params = {name: "user_1"}
      post "/users/create", @params
    end

    it "statusが200" do
      last_response.should be_ok
    end

    it "nameが正常" do
      json = JSON.parse last_response.body
      json["name"] == @params[:name]
    end
  end
end
