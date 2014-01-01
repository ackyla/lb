# -*- coding: utf-8 -*-
require 'spec_helper'

describe "TerritoriesController" do
  describe "#create" do
    before do
      @char = create(:character)
      @user = create(:user)
      params = {latitude: 100, longitude: 100, character_id: @char.id, user_id: @user.id, token: @user.token}
      post "/territories/create", params
      @json = JSON.parse last_response.body
      @ter = Territory.find(@json["territory"]["territory_id"])
      @user = User.find(@json["user"]["user_id"])
    end

    it "レスポンス チェック" do
      expect(last_response).to be_ok
    end

    it "テリトリーの検証" do
      expect(@user.my_territories).to include(@ter)
      expect(@ter["radius"]).to eq(@char.radius)
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
