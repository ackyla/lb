# -*- coding: utf-8 -*-
require 'spec_helper'

describe Territory do
  describe "Destroy" do
    before do
      @ter = build(:territory)
    end

    it "Destroy" do
      time = DateTime.now
      @ter.expire
      expired_time = @ter.expired_time
      expired_time != nil and time < expired_time
    end
  end

  describe "Detectionチェック" do
    before do
      @loc = build(:location)
      @loc2 = build(:location2)
      @ter = build(:territory)
    end

    context "範囲内" do
      it "include check" do
        @ter.include? @loc
      end
      it "add check" do
        pre_size = @ter.locations.size
        @ter.add_location(@loc)
        @ter.locations.size == pre_size + 1
      end
    end

    context "範囲外" do
      it "include check" do
        not @ter.include? @loc2
      end

      it "add check" do
        pre_size = @ter.locations.size
        @ter.add_location(@loc2)
        @ter.locations.size == pre_size
      end
    end
  end
end
