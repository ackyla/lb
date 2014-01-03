# -*- coding: utf-8 -*-
require 'spec_helper'

describe "TerritoriesController" do
  let(:user) { create(:user) }
  let(:ter) { create(:territory) }
  let(:char) { create(:character) }

  describe "#create" do
    before do
      params = {latitude: 100, longitude: 100, character_id: char.id, user_id: user.id, token: user.token}
      post "/territories/create", params
      user.reload
    end

    it "レスポンス チェック" do
      expect(last_response).to be_ok
    end

    it "テリトリーの検証" do
      json = JSON.parse last_response.body
      created_territory = Territory.find(json["territory"]["territory_id"])
      expect(user.my_territories).to include(created_territory)
      expect(created_territory.radius).to eq(char.radius)
    end
  end

  describe "#detections" do
    let(:loc) { create(:location) }

    before do
      @det = Detection.create(territory: ter, location: loc)
      ter.detections << @det
      user.my_territories << ter
      ter.save
      ter.reload

      params = {id: ter.id, user_id: user.id, token: user.token}
      get "/territories/detections", params
    end

    it "レスポンスチェック" do
      expect(last_response).to be_ok
    end

    it "Detectionが取得できる" do
      json = JSON.parse last_response.body
      expect(json["locations"].map{|l| l["id"]}).to include(loc.id)
    end
  end

  describe "#move" do
    it "move territory" do
      user.my_territories << ter
      params = {
        latitude: 0, longitude: 0,
        id: ter.id, user_id: user.id, token: user.token
      }

      post "/territories/move", params
      ter.reload

      expect(last_response).to be_ok
      expect(ter.latitude).to eq(params[:latitude])
      expect(ter.longitude).to eq(params[:longitude])
    end
  end

  describe "#supply" do
    let(:params) { {user_id: user.id, token: user.token, id: ter.id, gps_point: 30} }

    before do
      user.my_territories << ter
      @expiration_date = ter.expiration_date
      post "/territories/supply", params
      ter.reload
    end

    it "レスポンスチェック" do
      expect(last_response).to be_ok
    end

    it "テリトリーの有効時間が増加する" do
      expect(ter.expiration_date).to be > @expiration_date
    end
  end
end
