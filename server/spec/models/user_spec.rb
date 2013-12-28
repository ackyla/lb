# -*- coding: utf-8 -*-
require 'spec_helper'

describe User do
  describe "User生成" do
    before do
      @user = build(:user)
    end

    it "saveできる" do
      @user.save.should be_true
    end

    it "idが正常" do
      @user.id == 1
    end

    it "nameが正常" do
      @user.name == "user1"
    end
  end

  describe "Location" do
    before do
      @user = create(:user)
    end

    it "location数" do
      @user.locations.size == 1
    end
  end
end
