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
end
