require 'spec_helper'

describe "TerritoriesController" do
  describe "/territories/create" do
    it "create territory" do
      char = create(:character)
      user = create(:user)
      params = {latitude: 100, longitude: 100, character_id: char.id, user_id: user.id, token: user.token}
      post "/territories/create", params
      json = JSON.parse last_response.body
      ter = Territory.find(json["id"])
      user.reload

      expect(last_response).to be_ok
      expect(user.my_territories).to include(ter)
    end
  end

  describe "/territories/move" do
    it "move territory" do
      user = create(:user)
      ter = create(:territory)
      user.my_territories << ter
      params = {latitude: 0, longitude: 0, id: ter.id}
      post "/territories/move", params
      ter.reload

      expect(last_response).to be_ok
      expect(ter.latitude).to eq(params[:latitude])
      expect(ter.longitude).to eq(params[:longitude])
    end
  end
end
