# -*- coding: utf-8 -*-
require 'spec_helper'

describe "RankController" do
  let(:user) { create(:user) }
  let(:user2) { create(:user2) }

  describe "#detections" do
    before do
      3.times{
        Notification.create(
          notification_type: "detection",
          user: user
          )
      }

      4.times{
        Notification.create(
          notification_type: "detection",
          user: user2
          )
      }

      get "/rank/detections"
    end

    it "ステータスチェック" do
      expect(last_response).to be_ok
    end

    it "検出数ランキングが取得できる" do
      json = JSON.parse last_response.body
      [user2, user].zip(json).each{|u, j|
        expect(j["user_id"]).to eq(u.id)
      }
    end
  end
end
