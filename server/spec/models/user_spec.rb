# -*- coding: utf-8 -*-
require 'spec_helper'

describe User do
  before do
    @user = build(:user)
  end

  describe "create" do
    it "save" do
      @user.save.should be_true
    end

    it "name" do
      expect(@user.name).to eq("user1")
    end

    it "level" do
      expect(@user.level).to eq(1)
    end

    it "exp" do
      expect(@user.exp).to eq(0)
    end

    it "gps point" do
      expect(@user.gps_point).to eq(0)
    end


    it "location" do
      expect(@user.locations.size).to eq(0)
      size = @user.locations.size
      loc = @user.add_location(35.0, 135.8)
      loc.save
      @user.reload
      expect(@user.locations.size).to eq(size + 1)
    end

    it "territory" do
      ters = @user.valid_territories
      ter = ters.first
      expect(ters.size).to eq(0)
    end
  end
end
