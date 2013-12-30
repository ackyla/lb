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
      @loc = create(:location)
      @notification = Notification.new(
        user: @user,
        location: @loc,
        notification_type: "entering"
        )
      @notification.save
      @params = {user_id: @user.id, token: @user.token}
      get "/users/notifications", @params
      @json = JSON.parse last_response.body
    end

    it "status check" do
      expect(last_response).to be_ok
      ret = @json[0]
      %w(longitude latitude).each{|key|
        expect(ret["location"][key]).to eq(@loc[key.to_sym])
      }
    end
  end
end
