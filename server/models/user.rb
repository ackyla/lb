require 'securerandom'

class User < ActiveRecord::Base
  has_many :locations, dependent: :destroy
  has_many :my_territories, class_name: "Territory", foreign_key: 'owner_id', dependent: :destroy
  has_many :invasions, dependent: :destroy
  has_many :enemy_territories, class_name: "Territory", through: :invasions, source: :territory
  has_many :notifications, dependent: :destroy
  MINIMUM_TIME_INTERVAL = 300

  before_create do
    token = SecureRandom.base64(16)
    self.token = token
  end

  def add_territory(latitude, longitude, character_id)
    character = Character.find(character_id)
    return unless character
    return if character.cost > self.gps_point

    ter = self.my_territories.new(latitude: latitude, longitude: longitude, character: character)
    return unless ter.within_range(locations.last)

    self.gps_point -= character.cost
    return (save and ter)
  end

  def add_location(loc)
    recent_location = locations.order('created_at desc').first
    if recent_location
      time_interval = Time.now - recent_location.created_at
      return if time_interval < MINIMUM_TIME_INTERVAL
    end
    locations << loc
    self.gps_point += 1 if self.gps_point < self.gps_point_limit
    save
  end

  def use_point(point)
    return false if point > self.gps_point
    self.gps_point -= point
    save
  end

  def add_exp(exp_point)
    self.exp += exp_point
    level_up if level_up?
    self.save
  end

  def to_hash
    hash = Hash[self.attributes]
    hash["user_id"] = self.id
    hash.delete "id"
    hash
  end

  private
  def level_up?
    self.exp > (self.level * 100)
  end

  def level_up
    self.gps_point = self.gps_point_limit
    self.level = self.exp / 100 + 1
  end
end
