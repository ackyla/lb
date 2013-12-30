# -*- coding: utf-8 -*-
require 'spec_helper'

describe "UsersController" do
  describe "/users/create" do
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

  describe "/users/notifications" do
    before do
      @user = create(:user)
      @det = create(:detection)
      @notification = Notification.new(user: @user, detection: @det)
      @notification.save
      @params = {user_id: @user.id, token: @user.token}
      get "/users/notifications", @params
      @json = JSON.parse last_response.body
    end

    it "response check" do
      expect(last_response).to be_ok
    end

    it "notification check" do
      expect(@json.size).to eq(1)
    end
  end
end
