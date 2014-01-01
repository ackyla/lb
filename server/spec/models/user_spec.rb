# -*- coding: utf-8 -*-
require 'spec_helper'

describe User do

  let(:char) { create(:character) }

  describe "#add_territory" do
    let(:user) { create(:user) }

    before do
      @point = user.gps_point
      @ter = user.add_territory(35.0, 135.8, char.id)
      user.reload
    end

    it "テリトリーが追加される" do
      expect(user.my_territories).to include(@ter)
    end

    it "GPSポイントが消費される" do
      expect(user.gps_point).to eq(@point - char.cost)
    end
  end
end
