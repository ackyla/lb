require 'spec_helper'

describe "LocationsController" do
  before do
    @user = create(:user)
    @ter = create(:territory)
    params = {user_id: @user.id, token: @user.token, latitude: 35.0, longitude: 135.8}
    post "/locations/create", params
  end

  it "status check" do
    last_response.should be_ok
  end

  it "location check" do
    json = JSON.parse last_response.body
    loc = Location.find_by_id(json["id"])
    det = Detection.find_by_location_id(loc)
    ter = loc.territories.first
    loc != nil and det != nil and ter == @ter
  end
end
