# -*- coding: utf-8 -*-
require 'spec_helper'

describe Territory do
  let (:ter) { create(:territory) }
  let (:char) { create(:character) }

  describe "#create" do
    it "radiusがキャラクターの値からコピーされる" do
      expect(ter.radius).to eq(char.radius)
    end

    it "precisionがキャラクターの値からコピーされる" do
      expect(ter.precision).to eq(char.precision)
    end
  end

  describe "#actives" do
    it "有効期限切れのテリトリーをかえさない" do
      ter.expiration_date = ter.created_at
      ter.save
      expect(Territory.actives).to_not include(ter)
    end
  end

  describe "#supply" do
    let(:user) { create(:user) }

    before do
      ter.owner = user
      ter.save
    end

    context "有効期限内の場合" do
      it "有効期限が延長される" do
        expiration_date = DateTime.now + 1.days
        ter.expiration_date = expiration_date
        ter.supply(10)
        expect(ter.expiration_date).to be > expiration_date
      end
    end

    context "有効期限切れの場合" do
      it "現在の時間から有効期限が延長される" do
        ter.expiration_date = DateTime.now - 1.days
        ter.supply(10)
        expect(ter.expiration_date).to be > DateTime.now
      end
    end
  end
end
