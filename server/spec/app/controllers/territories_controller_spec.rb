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
end
