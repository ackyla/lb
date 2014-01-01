# -*- coding: utf-8 -*-
require 'spec_helper'

describe "LocationsController" do
  describe "#create" do
    before do
      @user = create(:user)
      @params = {
        user_id: @user.id,
        token: @user.token,
        latitude: 35.0,
        longitude: 135.8
      }
      @pre_point = @user.gps_point
      @user2 = create(:user2)
      @ter = create(:territory)
      @user2.my_territories << @ter
      post "/locations/create", @params
      @user.reload
    end

    it "レスポンス チェック" do
      expect(last_response).to be_ok
    end

    it "ロケーションの生成" do
      json = JSON.parse last_response.body
      loc = Location.find(json["location"]["id"])
      expect(loc).not_to be_nil
      expect(@user.locations).to include(loc)
    end

    it "侵入状態の更新" do
      expect(@user.enemy_territories).to include(@ter)
      expect(@ter.invaders).to include(@user)
    end

    it "陣力の増加" do
      expect(@pre_point).to be < @user.gps_point
    end

    describe "連続して#createした時" do
      before do
        @pre_point2 = @user.gps_point
        post "/locations/create", @params
        @user.reload
      end

      it "レスポンス チェック" do
        expect(last_response).to be_ok
      end

      it "陣力が増加しない" do
        expect(@user.gps_point).to eq(@pre_point2)
      end
    end
  end
end
