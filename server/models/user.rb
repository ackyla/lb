require 'securerandom'

class User < ActiveRecord::Base
  before_create :generate_token
  has_many :locations, dependent: :destroy
  has_many :my_territories, class_name: "Territory", foreign_key: 'owner_id', dependent: :destroy
  has_many :invasions, dependent: :destroy
  has_many :enemy_territories, class_name: "Territory", through: :invasions, source: :territory
  has_many :notifications, dependent: :destroy

  def valid_territories
    my_territories.where :expired_time => nil
  end

  def generate_token
    token = SecureRandom.base64(16)
    self.token = token
  end

  def add_location(latitude, longitude)
    loc = Location.new(latitude: latitude, longitude: longitude){|loc|
      loc.user = self
    }

    self.gps_point += 1 if self.gps_point < self.gps_point_limit
    self.save
    loc
  end

  def supply(ter, point)
    return "error" if point > self.gps_point
    self.gps_point -= point
    ter.supply(point) and self.save
  end

  def to_hash
    hash = Hash[self.attributes]
    hash["user_id"] = self.id
    hash.delete "id"
    hash
  end
end
