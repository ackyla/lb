# -*- coding: utf-8 -*-
require 'spec_helper'

describe User do
  before do
    @user = build(:user)
  end

  describe "User生成" do
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
    it "location数" do
      @user.locations.size == 1
    end
  end

  describe "Territory" do
    it "get valid territories" do
      ters = @user.valid_territories
      ter = ters.first
      ters.size == 1 and first.id == 1
    end
  end
end
