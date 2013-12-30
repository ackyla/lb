require 'spec_helper'

describe "NotificationsController" do
  before do
    @user = create(:user)
    @det = create(:detection)
    @notification = Notification.new(user: @user, detection: @det)
    @notification.save
    @params = {
      user_id: @user.id,
      token: @user.token,
      "notification_ids[]" => [@notification.id]
    }
    post '/notifications/read', @params
    @json = JSON.parse last_response.body
  end

  it "status check" do
    expect(last_response).to be_ok
    Notification.where(id: @json.map{|n| n["id"]}).each{|n|
      expect(n.read).to eq(true)
    }
  end
end
