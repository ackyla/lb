# -*- coding: utf-8 -*-
require 'spec_helper'

describe "NotificationsController" do
  let(:user) { create(:user) }
  let(:loc) { create(:location) }

  let(:ter) { create(:territory) }
  let(:user2) {
    u = create(:user2)
    u.territories << ter
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
