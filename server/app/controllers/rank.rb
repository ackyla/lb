Server::App.controllers :rank do
  get :detections, provides: :json do
    JSON.unparse Notification
      .where(notification_type: "detection")
      .group(:user_id)
      .order("count_user_id desc")
      .limit(10)
      .count("user_id")
      .map{|k, v| User.find(k).to_hash}
  end
end
