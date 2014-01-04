class AddDetectionCountToTerritories < ActiveRecord::Migration
  def self.up
    change_table :territories do |t|
      t.integer :detection_count, default: 0
    end
  end

  def self.down
    change_table :territories do |t|
      t.remove :detection_count
    end
  end
end
