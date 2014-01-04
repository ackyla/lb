# -*- coding: utf-8 -*-
require 'spec_helper'

describe "LocationsController" do
  let(:loc) { create(:location) }

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
      @user2.locations << loc
      @ter = build(:territory)
      @user2.my_territories << @ter
      @ter.save
      @user2.save
      post "/locations/create", @params

      @user.reload
      @ter.reload
    end

    it "レスポンス チェック" do
      expect(last_response).to be_ok
    end

    it "ロケーションの生成" do
      json = JSON.parse last_response.body
      loc = Location.find(json["location"]["id"])
      expect(json["status"]).to eq("ok")
      expect(loc).not_to be_nil
      expect(@user.locations).to include(loc)
    end

    it "侵入状態の更新" do
      expect(@user.enemy_territories).to include(@ter)
      expect(@ter.invaders).to include(@user)
      expect(@ter.detection_count).to eq(1)
    end

    it "陣力の増加" do
      expect(@user.gps_point).to be > @pre_point
    end

    describe "連続して#createした時" do
      before do
        @pre_point2 = @user.gps_point
        post "/locations/create", @params
        @user.reload
        @json = JSON.parse last_response.body
      end

      it "レスポンス チェック" do
        expect(last_response).to be_ok
      end

      it "ステータスがfailure" do
        expect(@json["status"]).to eq("failure")
      end

      it "陣力が増加しない" do
        expect(@user.gps_point).to eq(@pre_point2)
      end
    end

    describe "更新時間間隔内での連続post" do
      it "2回目は失敗する" do
        json = JSON.parse last_response.body
        over_interval_time = (User::MINIMUM_TIME_INTERVAL + 10).seconds
        loc = Location.find(json["location"]["id"])
        loc.update_attributes(created_at: DateTime.now - over_interval_time)

        ["ok", "failure"].each{|status|
          post "/locations/create", @params
          @user.reload

          expect(last_response).to be_ok
          json = JSON.parse last_response.body
          expect(json["status"]).to eq(status)
          expect(@user.locations.all.size).to eq(2)
        }
      end
    end
  end
end
