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

  describe "/users/show" do
    before do
      @user = create(:user)
      @params = {user_id: @user.id}
      get "/users/show", @params
      @json = JSON.parse last_response.body
    end

    it "status check" do
      expect(last_response).to be_ok
    end

    it "validate avatar url" do
      expect(last_response).to be_ok
    end
  end

  describe "/users/notifications" do
    before do
      @user = create(:user)
      @user2 = create(:user2)
      @loc = create(:location)
      @ter = create(:territory)
      @user2.my_territories << @ter
      @det = Detection.new(location: @loc, territory: @ter)
      @det.save
      @notification = Notification.new(
        user: @user,
        detection: @det,
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
      %w(user_id, name).each{|k|
        expect(ret["territory_owner"][k]).to eq(@user2[k.to_sym])
      }
    end
  end

  describe "/users/locations" do
    before do
      @user = create(:user)
      @user2 = create(:user2)

      @loc1 = create(:location)
      @loc1.update_attributes(:user => @user)
      @loc2 = create(:location2)
      @loc2.update_attributes(:user => @user)
      @loc3 = create(:location)
      @loc3.update_attributes(:user => @user, :created_at => (DateTime.now-1))

      @loc4 = create(:location)
      @loc4.update_attributes(:user => @user2)

      @params = {user_id: @user.id, token: @user.token, date: DateTime.now.strftime("%Y-%m-%dT00:00:00%Z")}
      get "/users/locations", @params
      @json = JSON.parse last_response.body
    end

    it "status check" do
      expect(last_response).to be_ok
      expect(@json.length).to eq(2)
    end
  end

  describe "/users/avatar" do
    before do
      @user = create(:user)
      @params = {user_id: @user.id, token: @user.token, avatar: File.open(Padrino.root('public/avatars', 'default_avatar.jpg')) }
      post "/users/avatar", @params
      @json = JSON.parse last_response.body
    end

    it "status check" do
      expect(last_response).to be_ok
      expect(@json["avatar"]).not_to be_nil
    end

    it "validate avatar url" do
      get @json["avatar"]
      expect(last_response).to be_ok
    end
  end
end
