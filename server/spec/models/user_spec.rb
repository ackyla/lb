# -*- coding: utf-8 -*-
require 'spec_helper'

describe User do

  let(:char) { create(:character) }
  let(:user) { create(:user) }

  describe "#add_territory" do
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

  describe "#add_location" do
    before do
      @pre_point = user.gps_point
      @loc = user.add_location(100, 100)
    end

    it "ロケーションが生成される" do
      expect(user.locations).to include(@loc)
    end

    it "GPSポイントが増える" do
      expect(user.gps_point).to be > @pre_point
    end
  end

  describe "#add_exp" do
    it "経験値が増加する" do
      user.add_exp 10
      expect(user.exp).to eq(10)
    end


    describe "レベルアップ処理" do
      before do
        user.add_exp 110
      end
      it "レベルアップする" do
        expect(user.level).to eq(2)
      end

      it "陣力が増加する" do
        expect(user.gps_point).to eq(user.gps_point_limit)
      end
    end
  end
end
