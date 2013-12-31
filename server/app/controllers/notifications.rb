Server::App.controllers :notifications do
  before :read do
    login params
    @notification = Notification.find_by_id(params[:notification_id])
  end


  post :read, :provides => :json do
    @notification.read = true
    @notification.save
    @notification.to_json
  end
end
