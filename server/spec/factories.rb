# -*- coding: utf-8 -*-
FactoryGirl.define do
  factory :user do
    initialize_with { User.where(id: 1, name: "user1").first_or_create }
    gps_point 500
    gps_point_limit 1000
  end

  factory :user2, class: User do
    initialize_with { User.where(id: 2, name: "user2").first_or_create }
  end

  factory :coordinate do
    initialize_with { Coordinate.find_or_create(lat: 35.0, long: 135.8) }
  end

  factory :location do
    association :coordinate
  end

  factory :location2, class: Location do
    association :coordinate
  end

  factory :character do
    name "テストキャラ"
    precision 1.0
    radius 1000
    distance 1000
    cost 100
  end

  factory :territory do
    association :coordinate
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
