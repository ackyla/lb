Server::App.controllers :locations do
  post :create, :provides => :json do
    login(params)
    loc = @user.add_location(params[:latitude], params[:longitude])
    if loc
      loc.save
      ters = Territory.actives.where("owner_id != ?", @user.id).select{|ter|
        ter.include? loc
      }

      enemies = @user.enemy_territories.to_a
      ters.each{|ter|
        ter.add_location loc
      }

      new_ters = (ters - enemies)
      Invasion.destroy_all(user_id: @user.id, territory_id: (enemies - ters).map{|t| t.id})

      new_ters.each{|ter|
        ter.detect(@user, loc)
      }
      response = {status: "ok", location: loc.to_hash}
    else
      response = {status: "failure"}
    end

    JSON.unparse({user: @user.to_hash}.merge(response))
  end
end
