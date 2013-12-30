Server::App.controllers :notifications do
  before :read do
    login params
    @notifications = Notification.where(
      user_id: @user.id,
      id: params[:notification_ids]
      )
  end


  post :read, :provides => :json do
    @notifications.map{|n|
      n.read = true
      n.save
      n
    }.to_json
  end
end
