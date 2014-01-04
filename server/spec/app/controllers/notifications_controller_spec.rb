# -*- coding: utf-8 -*-
require 'spec_helper'

describe "NotificationsController" do
  let(:user) { create(:user) }
  let(:loc) { create(:location) }
  let(:user2) {
    u = build(:user2)
    u.locations << loc
    u.save
    u
  }
  let(:ter) {
    t = build(:territory)
    t.owner = user2
    t.save
    t
  }


  describe "#read" do
    it "レスポンスチェック" do
      det = Detection.create(location: loc, territory: ter)
      notif = Notification.new(user: user, detection: det, notification_type: "entering")
      notif.save
      params = {
        user_id: user.id,
        token: user.token,
        notification_id: notif.id
      }
      post '/notifications/read', params
      notif.reload

      expect(last_response).to be_ok
      expect(notif.read).to eq(true)
    end
  end
end
