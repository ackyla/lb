Server::App.controllers :locations do
  post :create, :provides => :json do
    login(params)
    loc = @user.add_location(params[:latitude], params[:longitude])
    loc.save

    ters = Territory.where("owner_id != ?", @user.id).select{|ter|
      ter.include? loc
    }

    enemies = @user.enemy_territories.to_a
    ters.each{|ter|
      ter.add_location loc
    }

    new_ters = (ters - enemies)
    Invasion.destroy_all(user_id: @user.id, territory_id: (enemies - ters).map{|t| t.id})

    new_ters.each{|ter|
      ter.invaders << @user
      det = Detection.where(location_id: loc.id, territory_id: ter.id).first
      Notification.new(notification_type: "entering", user: @user, detection: det).save
      Notification.new(notification_type: "detection", user: ter.owner, detection: det).save
    }

    loc.to_json
  end
end
