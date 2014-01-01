# -*- coding: utf-8 -*-
require 'spec_helper'

describe Territory do
  describe "#create" do
    it "territory.radius == character.radius" do
      ter = create(:territory)
      char = create(:character)
      expect(ter.radius).to eq(char.radius)
    end
  end
end
