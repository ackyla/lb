FactoryGirl.define do
  factory :user do
    name "user"
  end

  factory :location do
    latitude 35.0
    longitude 135.8
    user
  end

  factory :location2, class: Location do
    latitude 0.0
    longitude 0.0
    user
  end

  factory :territory do
    latitude 35.0
    longitude 135.8
    radius 10
    user
  end
end
