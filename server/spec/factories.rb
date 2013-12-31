# -*- coding: utf-8 -*-
FactoryGirl.define do
  factory :user do
    name "user1"
  end

  factory :user2, class: User do
    name "user2"
  end

  factory :location do
    latitude 35.0
    longitude 135.8
  end

  factory :location2, class: Location do
    latitude 0.0
    longitude 0.0
  end

  factory :character do
    name "テストキャラ"
    radius 1000
    precision 1.0
  end

  factory :territory do
    latitude 35.0
    longitude 135.8
    radius 10
    character
  end

  factory :detection do
    location
    territory
  end

  factory :expired_territory do
    latitude 35.0
    longitude 135.8
    radius 10
  end
end
