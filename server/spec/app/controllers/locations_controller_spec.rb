require 'spec_helper'

describe "LocationsController" do
  before do
    @user = create(:user)
    @user2 = create(:user2)
    @ter = create(:territory)
    @user2.my_territories << @ter

    @params1 = {user_id: @user.id, token: @user.token, latitude: 35.0, longitude: 135.8}
    post "/locations/create", @params1
    @res1 = last_response
    @json1 = JSON.parse last_response.body
    @loc1 = Location.find_by_id(@json1["id"])

    @params2 = {user_id: @user.id, token: @user.token, latitude: 35.0, longitude: 135.8}
    post "/locations/create", @params2
    @res2 = last_response
    @json2 = JSON.parse last_response.body
    @loc2 = Location.find_by_id(@json2["id"])
  end

  it "status check" do
    @res1.should be_ok
    @res2.should be_ok
  end

  it "location check" do
    expect(@loc1.latitude).to eq(@params1[:latitude])
    expect(@loc1.longitude).to eq(@params1[:longitude])
  end

  it "detection check" do
    expect(@loc1.territories.first).to eq(@ter)
    expect(@loc2.territories.first).to eq(@ter)
  end

  it "enemy territory check" do
    expect(@user.enemy_territories.first).to eq(@ter)
  end

  it "invader check" do
    expect(@ter.invaders.first).to eq(@user)
    expect(Invasion.all.size).to eq(1)
  end
end
