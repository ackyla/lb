require 'spec_helper'

describe "LocationsController" do
  it "status check" do
    user = create(:user)
    user2 = create(:user2)
    ter = create(:territory)
    user2.my_territories << ter

    point = user.gps_point
    params = {user_id: user.id, token: user.token, latitude: 35.0, longitude: 135.8}
    post "/locations/create", params
    json = JSON.parse last_response.body
    loc = Location.find_by_id(json["location"]["id"])
    user.reload

    expect(last_response).to be_ok
    expect(loc.latitude).to eq(params[:latitude])
    expect(loc.longitude).to eq(params[:longitude])

    expect(loc.territories).to include(ter)
    expect(user.enemy_territories).to include(ter)
    expect(ter.invaders).to include(user)
    expect(user.gps_point).to eq(point + 1)
  end
end
